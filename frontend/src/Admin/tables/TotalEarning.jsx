// ** MUI Imports
import Box from '@mui/material/Box'
import Card from '@mui/material/Card'
import Avatar from '@mui/material/Avatar'
import Typography from '@mui/material/Typography'
import IconButton from '@mui/material/IconButton'
import CardHeader from '@mui/material/CardHeader'
import CardContent from '@mui/material/CardContent'
import LinearProgress from '@mui/material/LinearProgress'

// ** Icons Imports
import MenuUp from 'mdi-material-ui/MenuUp'
import DotsVertical from 'mdi-material-ui/DotsVertical'

const data = [
  {
    progress: 75,
    imgHeight: 20,
    title: 'Makeup',
    color: 'primary',
    amount: '$24,895.65',
    subtitle: 'Foundation, Lipstick',
    imgSrc: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqmXrOibsIn98rVZ_Vg6WnLwjbS6PQitj3cg&s'
  },
  {
    progress: 50,
    color: 'info',
    imgHeight: 27,
    title: 'Skincare',
    amount: '$8,650.20',
    subtitle: 'Sunscreen, Serums',
    imgSrc: 'data:image/jpeg;base64,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'
  },
  {
    progress: 20,
    imgHeight: 20,
    title: 'Haircare',
    color: 'secondary',
    amount: '$1,245.80',
    subtitle: 'Conditioners, Hairoils',
    imgSrc: 'data:image/jpeg;base64,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'
  }
]

const TotalEarning = () => {
  return (
    <Card>
      <CardHeader
        title='Total Earning'
        titleTypographyProps={{ sx: { lineHeight: '1.6 !important', letterSpacing: '0.15px !important' } }}
        action={
          <IconButton size='small' aria-label='settings' className='card-more-options' sx={{ color: 'text.secondary' }}>
            <DotsVertical />
          </IconButton>
        }
      />
      <CardContent sx={{ pt: theme => `${theme.spacing(1.5)} !important` }}>
        <Box sx={{ mb: 1.5, display: 'flex', alignItems: 'center' }}>
          <Typography variant='h4' sx={{ fontWeight: 600, fontSize: '2.125rem !important' }}>
            $24,895
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', color: 'success.main' }}>
            <MenuUp sx={{ fontSize: '1.875rem', verticalAlign: 'middle' }} />
            <Typography variant='body2' sx={{ fontWeight: 600, color: 'success.main' }}>
              10%
            </Typography>
          </Box>
        </Box>

        <Typography component='p' variant='caption' sx={{ mb: 5 }}>
          Compared to $84,325 last year
        </Typography>

        {data.map((item, index) => {
          return (
            <Box
              key={item.title}
              sx={{
                display: 'flex',
                alignItems: 'center',
                ...(index !== data.length - 1 ? { mb: 4 } : {})
              }}
            >
              <Avatar
                variant='rounded'
                sx={{
                  mr: 3,
                  width: 40,
                  height: 40,
                  
                }}
              >
                <img src={item.imgSrc} alt={item.title} height={item.imgHeight} />
              </Avatar>
              <Box
                sx={{
                  width: '100%',
                  display: 'flex',
                  flexWrap: 'wrap',
                  alignItems: 'center',
                  justifyContent: 'space-between'
                }}
              >
                <Box sx={{ marginRight: 2, display: 'flex', flexDirection: 'column' }}>
                  <Typography variant='body2' sx={{ mb: 0.5, fontWeight: 600, color: 'text.primary' }}>
                    {item.title}
                  </Typography>
                  <Typography variant='caption'>{item.subtitle}</Typography>
                </Box>

                <Box sx={{ minWidth: 85, display: 'flex', flexDirection: 'column' }}>
                  <Typography variant='body2' sx={{ mb: 2, fontWeight: 600, color: 'text.primary' }}>
                    {item.amount}
                  </Typography>
                  
                </Box>
              </Box>
            </Box>
          )
        })}
      </CardContent>
    </Card>
  )
}

export default TotalEarning
